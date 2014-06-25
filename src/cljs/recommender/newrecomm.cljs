(ns recommender.newrecomm
  (:require [domina :as dom]
            [hiccups.runtime :as hiccupsrt]
            [domina.events :as ev]
            [ajax.core :refer [GET POST]]
            ;[recommender.utils.validation :refer [new-recomm-errors]]
            ))

#_(defn validate-service-name [sn]
  ;(GET "/hello" {:params {:foo "foo"}})
  true)

#_(defn validate-form [evt service-name provider-name location tags]
  (if-let [{s-errs :service-name p-errs :provider-name l-errs :location t-errs :tags}
           (new-recomm-errors (dom/value service-name) (dom/value provider-name) (dom/value location) (dom/value tags))
           ;{su-errs :service-name} (service-name-errors (dom/value service-name))
           ]
    (if (or s-errs p-errs l-errs t-errs)
      (do
        (dom/destroy! (dom/by-class "help"))
        (ev/prevent-default evt)
        (dom/append! (dom/by-id "loginForm") (hiccupsrt/html [:div.help "Please complete the form."])))
      (ev/prevent-default evt))
    true))

(defn handler [response]
  (dom/prepend! (dom/by-id "form-content") (hiccupsrt/render-html [:div#success.alert.alert-success
                                                                  [:button.close {:data-dismiss "alert", :type "button"} "×"]
                                                                  [:h4 "Success!"]
                                                                  [:p  response]])))

(defn err-handler [response]
  (dom/prepend! (dom/by-id "form-content") (hiccupsrt/render-html [:div#error.alert.alert-block
                                                                   [:button.close {:data-dismiss "alert", :type "button"} "×"]
                                                                   [:h4 "Error!"]
                                                                   [:p (:response response)]])))


(defn submit-form [evt service-name provider-name location tags]
  ;(if (validate-form evt service-name provider-name location tags)
  (dom/destroy! (dom/by-id "error"))
  (dom/destroy! (dom/by-id "success"))
  (POST "/add-new-recomm"
              {:format :raw
               :params {:service-name (dom/value service-name)
                        :provider-name (dom/value provider-name)
                        :location (dom/value location)
                        :tags (dom/value tags)}
               :handler handler
               :error-handler err-handler})
  (ev/prevent-default evt)

      )



;)


(defn ^:export init []
  (if (and js/document
           (aget js/document "getElementById"))
    (let [service-name (dom/by-id "service-name") provider-name (dom/by-id "provider-name")
          location (dom/by-id "location") tags (dom/by-id "tags")]
      (ev/listen! (dom/by-id "submit") :click
                  (fn [evt] (submit-form evt service-name provider-name location tags))
                  )
      ;(ev/listen! service-name :blur (fn [evt] (validate-service-name service-name)))
      )))
