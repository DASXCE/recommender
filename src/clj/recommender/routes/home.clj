(ns recommender.routes.home
  (:require [compojure.core :refer :all]
            [liberator.core :refer [defresource resource]]
            [noir.io :as io]
            [clojure.java.io :refer [file]]
            [recommender.utils.validation :refer :all]
            [recommender.views.errors :refer :all]
            [recommender.models.db :as d]))

(defresource home
  :available-media-types ["text/html"]

  :exists?
  (fn [context]
    [(io/get-resource "/index.html")
     {::file (file (str (io/resource-path) "/index.html"))}])

  :handle-not-found
  (fn [{{{resource :resource} :route-params} :request}]
    (clojure.java.io/input-stream (io/get-resource "/error.html")))

  :handle-ok
  (fn [{{{resource :resource} :route-params} :request}]
    (clojure.java.io/input-stream (io/get-resource "/index.html")))

  :last-modified
  (fn [{{{resource :resource} :route-params} :request}]
    (.lastModified (file (str (io/resource-path) "/index.html")))))


(defresource add-new-recomm
  :allowed-methods [:post]

  :malformed?
  (fn [context]
    (let [{:keys [service-name provider-name location tags]} (get-in context [:request :params])]
      (if (and (empty? (new-recomm-errors service-name provider-name location tags))
               (empty? (service-name-errors service-name)))
        false
        true)
      (println service-name provider-name location tags)))

  :handle-malformed
  "The form did not pass the validation!"

  :post!
  (fn [context]
    (let [{:keys [service-name provider-name location tags]} (get-in context [:request :params])
          tags (vec (.split tags " "))]

      (d/add-new-recomm service-name provider-name location tags)))

  :handle-created (fn [_] "ok")

  :available-media-types ["text/html"]
  )

(defroutes home-routes
  (ANY "/" request home)
  (ANY "/add-new-recomm" request add-new-recomm))
