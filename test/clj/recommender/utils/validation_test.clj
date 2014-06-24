(ns recommender.utils.validation-test
  (:require [clojure.test :refer [deftest are testing]]
            [recommender.utils.validation :refer :all]))

(deftest validate-new-recomm-form
  (testing "New recommendation form"
    (testing "Pass?"
      (are [expected actual] (= expected actual)
          nil (new-recomm-errors "testpreporuka" "goran" "bijela" ["test1" "test2" "..."])))))
