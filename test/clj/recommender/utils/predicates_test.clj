(ns recommender.utils.predicates-test
  (:require [clojure.test :refer [deftest are testing]]
            [recommender.utils.predicates :refer :all]))

(deftest validate-service-name-exists?
  (testing "Service exists predicate function"
    (testing "Pass?"
      (are [expected actual] (= expected actual)
          true (service-unique? "this shouldn't exist in db!")))))
