(ns recommender.models.db-test
  (:require [clojure.test :refer [deftest are testing]]
            [recommender.models.db :refer :all]))

(deftest validate-service-exists?
  (testing "Service exists db query function"
    (testing "Pass?"
      (are [expected actual] (= expected actual)
          true (service-exists? "Popravka racunara")))
    (testing "Fail!"
      (are [expected actual] (= expected actual)
           ;; this is in seed data
          false (service-exists? "this shouldn't exist in db!")))))

(deftest persist-new-recomm
  (testing "Persisting new recommendation"
    (testing "Pass"
      (are [expected actual] (= expected actual)
           (add-new-recomm "Test-Service" "Test-Provider" "Test-Location" ["test-tg1" "test-tg2" "test-tg3" "..."])
           (get-new-recomm)))))
