(ns dda.pallet.webmeeting.infra.jitsi
  (:require
   [schema.core :as s]
   [dda.pallet.dda-k8s-crate.infra.transport :as transport]))

(s/def Jitsi {:fqdn s/Str
              :secret-name s/Str
              :cluster-issuer s/Str})

(def JitsiInfra {:jitsi Jitsi})

(def jitsi "jitsi")

(defn init
  [facility user config]
  (let [facility-name (name facility)]
    (transport/log-info facility-name "init")
    (transport/copy-resources-to-tmp
     facility-name jitsi
     [{:filename "install-as-root.sh" :config {:user user}}])))


(defn install
  [facility user config]
  (let [facility-name (name facility)]
    (transport/log-info facility-name "install")
    (transport/copy-resources-to-user
     user facility-name jitsi
     [{:filename "pod-running.sh"}
      {:filename "jitsi" :config config}
      {:filename "jitsi-storage.yml"}
      {:filename "ingress-jitsi.yml" :config config}
      {:filename "jitsi-k8s.py" :config config}
      {:filename "configure-as-user.sh"}
      {:filename "verify.sh" :config config}])
    (transport/exec
     facility-name jitsi "install-as-root.sh")))

(defn configure
  [facility user config]
  (let [facility-name (name facility)]
    (transport/log-info facility-name "configure")
    (transport/exec-as-user
     user facility-name jitsi "configure-as-user.sh")))
