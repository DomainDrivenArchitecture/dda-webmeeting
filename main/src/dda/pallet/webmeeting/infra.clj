(ns dda.pallet.webmeeting.infra
  (:require
   [schema.core :as s]
   [dda.pallet.core.infra :as core-infra]
   [dda.pallet.webmeeting.infra.jitsi :as jitsi]))

(def facility :webmeeting)

(def WebmeetingInfra 
  (merge 
   {:user s/Keyword}
   jitsi/JitsiInfra))

(s/defmethod core-infra/dda-init facility
  [dda-crate config]
  (let [facility (:facility dda-crate)
        {:keys [user jitsi]} config
        user-str (name user)]
    ;(jitsi/init facility user-str jitsi)
    ))

(s/defmethod core-infra/dda-install facility
  [dda-crate config]
  (let [facility (:facility dda-crate)
        {:keys [user jitsi]} config
        user-str (name user)]
    ;(jitsi/install facility user-str jitsi)
    ))

(s/defmethod core-infra/dda-configure facility
  [dda-crate config]
  (let [facility (:facility dda-crate)
        {:keys [user jitsi]} config
        user-str (name user)]
    ;(jitsi/configure facility user-str jitsi)
    ))

(def dda-webmeeting
  (core-infra/make-dda-crate-infra
   :facility facility
   :infra-schema WebmeetingInfra))

(def with-webmeeting
  (core-infra/create-infra-plan dda-webmeeting))
