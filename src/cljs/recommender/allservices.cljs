(ns recommender.allservices
  (:require [domina :as dom]
            [hiccups.runtime :as hiccupsrt]
            [domina.events :as ev]
            [ajax.core :refer [GET POST]]))

(defn render-service [{:keys [service-name provider-name location tags]}]
  [:div.span3
   [:div.widget
    [:div.widget-content
     [:h3 service-name]
     [:p "Provider: " provider-name]
     [:p "Location: " location]
     [:p (map #(vec [:span.label.label-info {:style "margin-right:2px;"} (str %)]) tags)]]]])

(defn handler [services]
  (dom/append! (dom/by-id "rowContent")
               (hiccupsrt/render-html (map render-service services))
               )
  )

(defn get-all-services []
  (GET "/get-all-services"
       {:handler handler}))

(defn ^:export init []
  (if (and js/document
           (aget js/document "getElementById"))

    (do
      (get-all-services))))

;(map #(str % " ") [1 2 3])

;(map render-service [{:location "dsfs", :tags ["b" "a" "s"], :service-name "dshlk", :e-id 17592186045466, :provider-name "sdsd"} ])


;(map #(vec [:span.label.label-info %]) ["a" "s"])

