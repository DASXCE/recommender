(ns recommender.newrecomm
  (:require [domina :as dom]
            [hiccups.runtime :as hiccupsrt]
            [domina.events :as ev]
            [ajax.core :refer [GET POST]]
            ;[recommender.utils.validation :refer [new-recomm-errors service-name-errors]]
            ))

#_(defn validate-service-name [sn]
  ;(GET "/hello" {:params {:foo "foo"}})
  true)

#_(defn validate-form [evt service-name provider-name location tags]
  (if-let [{s-errs :service-name p-errs :provider-name l-errs :location t-errs :tags}
           (new-recomm-errors (dom/value service-name) (dom/value provider-name) (dom/value location) (dom/value tags))
           ;{su-errs :service-name} (service-name-errors (dom/value service-name))
           ]
    (if (or s-errs p-errs l-errs t-errs su-errs)
      (do
        (dom/destroy! (dom/by-class "help"))
        (ev/prevent-default evt)
        (dom/append! (dom/by-id "loginForm") (h/html [:div.help "Please complete the form."])))
      (ev/prevent-default evt))
    true))

#_(defn ^:export init []
  (if (and js/document
           (aget js/document "getElementById"))
    (let [service-name (dom/by-id "service-name") provider-name (dom/by-id "provider-name")
          location (dom/by-id "location") tags (dom/by-id "tags")]
      (ev/listen! (dom/by-id "submit") :click (fn [evt] (validate-form evt service-name provider-name location tags)))
      (ev/listen! service-name :blur (fn [evt] (validate-service-name service-name))))))
