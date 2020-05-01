(ns dda.pallet.webmeeting.app
  (:require
   [schema.core :as s]
   [dda.pallet.commons.secret :as secret]
   [dda.config.commons.map-utils :as mu]
   [dda.pallet.core.app :as core-app]
   [dda.pallet.dda-config-crate.infra :as config-crate]
   [dda.pallet.dda-user-crate.app :as user]
   [dda.pallet.dda-k8s-crate.app :as k8s]
   [dda.pallet.webmeeting.infra :as infra]
   [dda.pallet.webmeeting.convention :as conv]))

(def with-webmeeting infra/with-webmeeting)

(def WebmeetingConvention conv/WebmeetingConvention)

(def WebmeetingConventionResolved conv/WebmeetingConventionResolved)

(def InfraResult conv/InfraResult)

(def WebmeetingApp
  {:group-specific-config
   {s/Keyword (merge InfraResult
                     user/InfraResult
                     k8s/InfraResult)}})

(s/defn ^:always-validate
  app-configuration-resolved :- WebmeetingApp
  [resolved-conv-config :- WebmeetingConventionResolved
   & options]
  (let [{:keys [group-key] :or {group-key infra/facility}} options]
    (mu/deep-merge
      (k8s/app-configuration-resolved
       (conv/k8s-convention-configuration resolved-conv-config) :group-key group-key)
      {:group-specific-config
       {group-key
        (conv/infra-configuration resolved-conv-config)}})))

(s/defn ^:always-validate
  app-configuration :- WebmeetingApp
  [conv-config :- WebmeetingConvention
   & options]
  (let [resolved-conv-config (secret/resolve-secrets conv-config WebmeetingConvention)]
    (apply app-configuration-resolved resolved-conv-config options)))

(s/defmethod ^:always-validate
  core-app/group-spec infra/facility
  [crate-app
   conv-config :- WebmeetingConventionResolved]
  (let [app-config (app-configuration-resolved conv-config)]
    (core-app/pallet-group-spec
     app-config [(config-crate/with-config app-config)
                 user/with-user
                 k8s/with-k8s
                 with-webmeeting])))

(def crate-app (core-app/make-dda-crate-app
                :facility infra/facility
                :domain-schema WebmeetingConvention
                :domain-schema-resolved WebmeetingConventionResolved
                :default-domain-file "webmeeting.edn"))
