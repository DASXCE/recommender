(ns recommender.views.errors
  (:require [net.cgrand.enlive-html :refer [deftemplate set-attr]]))

(deftemplate update-error-page "public/error.html"
  [http-errc] ; added errors argument
  [:#http-errc] (set-attr :value http-errc))

;; new intermediate function
(defn error-page [http-errc]
  (update-error-page http-errc))
