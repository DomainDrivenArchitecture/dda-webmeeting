(ns dda.pallet.webmeeting.convention
  (:require
   [schema.core :as s]
   [dda.pallet.commons.secret :as secret]
   [dda.config.commons.map-utils :as mu]
   [dda.pallet.dda-k8s-crate.domain :as k8s-conv]
   [dda.pallet.webmeeting.infra :as infra]
   [clojure.string :as str]))

(def InfraResult {infra/facility infra/WebmeetingInfra})

(s/def WebmeetingConvention
  {:user s/Keyword
   :fqdn s/Str
   :external-ip s/Str
   :cert-manager (s/enum :letsencrypt-prod-issuer :letsencrypt-staging-issuer)})

(def WebmeetingConventionResolved (secret/create-resolved-schema WebmeetingConvention))

(s/defn k8s-convention-configuration :- k8s-conv/k8sDomainResolved
  [convention-config :- WebmeetingConventionResolved]
  (let [{:keys [cert-manager external-ip user]} convention-config
        cluster-issuer (name cert-manager)]
    {:user user
     :k8s {:external-ip external-ip}
     :cert-manager cert-manager}))

(s/defn ^:always-validate
  infra-configuration :- InfraResult
  [convention-config :- WebmeetingConventionResolved]
  (let [{:keys [cert-manager fqdn db-user-password user]} convention-config
        cluster-issuer (name cert-manager)]
    {infra/facility
      {:user user
       :jitsi {:fqdn fqdn
              :secret-name (str/replace fqdn #"\." "-")
              :cluster-issuer cluster-issuer}
       
      }}))

