(ns recommender.models.db
  (:require [datomic.api :only [db q] :as d]))

(def uri "datomic:free://localhost:4334/recommender")

(d/create-database uri)

;; (d/delete-database uri)

(def conn (d/connect uri))

;(def db-val (d/db conn))

(def schema (read-string (slurp "src/clj/recommender/models/schema.edn")))

(def seed (read-string (slurp "src/clj/recommender/models/seed-data.edn")))

(d/transact conn schema)

(defn install-seed-data []
  (d/transact conn seed))

;; (install-seed-data)


(defn get-all-services []
  (d/q '[:find ?n :where [?c service/name ?n ]] (d/db conn)))

(defn get-all-tags []
  (d/q '[:find ?n :where [?c service/tags ?n ]] (d/db conn)))

(defn get-all-providers []
  (d/q '[:find ?n ?c :where [?c provider/name ?n ]
                                ] (d/db conn)))

;(install-seed-data)
;(get-all-services)

(defn service-exists? [value]
  (if (empty? (d/q '[:find ?value :in $ ?value :where [?e :service/name ?value]] (d/db conn) value))
    false
    true))

(defn add-new-recomm [service-name provider-name location tags]
  (let [s-id (d/tempid :db.part/user)
        p-id (d/tempid :db.part/user)
        l-id (d/tempid :db.part/user)
        tags (set tags)]
    (d/transact conn [{:db/id s-id
                       :service/name service-name
                       :service/tags tags
                       :service/provider p-id}

                      {:db/id p-id
                       :provider/name provider-name
                       :provider/location l-id
                       :provider/service s-id}

                      {:db/id l-id
                       :location/name location}])
    [service-name provider-name location tags]))

 ;(add-new-recomm "Test-Service12" "Test-Provider1" "Test-Location1" ["t-tg1" "t-tg2" "t-tg3" "..."])

(defn get-last-added-service
  []
  (-> (sort-by #(second %) (d/q '[:find ?e ?ts
                                  :where
                                  [?e :service/name ?n ?tx]
                                  [?tx :db/txInstant ?ts]] (d/db conn)))
      (reverse)
      (ffirst)))

(defn get-service-name [s-id]
  (ffirst (d/q '[:find ?n :in $ ?s-id :where [?s-id :service/name ?n]] (d/db conn) s-id)))
(defn get-provider-name [p-id]
  (ffirst (d/q '[:find ?n :in $ ?p-id :where [?s-id :provider/name ?n]] (d/db conn) p-id)))
(defn get-location-name [l-id]
  (ffirst (d/q '[:find ?n :in $ ?l-id :where [?l-id :location/name ?n]] (d/db conn) l-id)))



(defn get-last-added-provider
  []
  (-> (sort-by #(second %) (d/q '[:find ?e ?ts
                                  :where
                                  [?e :provider/name ?n ?tx]
                                  [?tx :db/txInstant ?ts]] (d/db conn)))
      (reverse)
      (ffirst)))

(defn get-last-added-location
  []
  (-> (sort-by #(second %) (d/q '[:find ?e ?ts
                                  :where
                                  [?e :location/name ?n ?tx]
                                  [?tx :db/txInstant ?ts]] (d/db conn)))
      (reverse)
      (ffirst)))

(defn get-tags-for-last-added-service
  []
  (->> (d/q '[:find ?n ?ts
              :in $ ?e
              :where
              [?e :service/tags ?n ?tx]
              [?tx :db/txInstant ?ts]] (d/db conn) (get-last-added-service))

       (sort-by #(second %))
       (reverse)
       (map #(first %))
       (set)))



; (get-service-name (get-last-added-service))
; (get-tags-for-last-added-service)
; (get-all-tags)
; (get-location-name (get-last-added-location))
; (get-provider-name (get-last-added-provider))

; (add-a-person "miliu")
; (get-all-providers)
; (get-all-services)

;(datomic.api/transact conn
 ;            [{:db/id (d/tempid :db.part/user) :location/name "Santiago, Chile" :provider/telephone "+56 9999 9999"}])

 (defn delete-last-added-entity []
   (d/transact conn [[:db.fn/retractEntity (get-last-added-service)]
                     [:db.fn/retractEntity (get-last-added-location)]
                     [:db.fn/retractEntity (get-last-added-provider)]]))

 (defn get-new-recomm []
   "Test helper function. Returns last added recommendation (service-name, provider-name, location, tags)
   Deletes last entity from db"
   (let [return [(get-service-name (get-last-added-service)) (get-provider-name (get-last-added-provider))
                 (get-location-name (get-last-added-location)) (get-tags-for-last-added-service)]]
     (delete-last-added-entity)
     return))

