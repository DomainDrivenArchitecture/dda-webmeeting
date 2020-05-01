(ns dda.pallet.webmeeting.app-test
  (:require
   [clojure.test :refer :all]
   [schema.core :as s]
   [dda.pallet.webmeeting.app :as sut]))

(s/set-fn-validation! true)

(s/def test-domain-conf
  {:user :k8s
   :external-ip "external-ip"
   :cert-manager :letsencrypt-staging-issuer
   :fqdn "jitsi.test.domaindrivenarchitecture.org"})

(deftest app-config
  (testing
   "test plan-def"
    (is (map? (sut/app-configuration-resolved test-domain-conf)))))

(deftest plan-def
  (testing
   "test plan-def"
    (is (map? sut/with-webmeeting))))
