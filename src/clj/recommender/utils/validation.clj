(ns recommender.utils.validation
  (:require [valip.core :refer [validate]]
            [valip.predicates :as p]
            [recommender.utils.predicates :as rp]))

(defn new-recomm-errors [service-name provider-name location tags]
  (validate {:service-name service-name :provider-name provider-name :location location :tags tags}

            ;; not empty?
            [:service-name p/present? "Service name can't be empty!"]
            [:provider-name p/present? "Provider name can't be empty!"]
            [:location p/present? "Location name can't be empty!"]))

(defn service-name-errors [service-name]
  (validate {:service-name service-name}
            [:service-name rp/service-unique? (str "Service name: '" service-name "' already exists!")]))
