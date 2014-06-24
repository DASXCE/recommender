(ns recommender.utils.predicates
  (:require [recommender.models.db :refer [service-exists?]]))

(defn service-unique?
  [n]
  (not (service-exists? n)))
