(ns map-to-xml.simple-xml-test
  (:require [clojure.test :refer [deftest testing is]]
            [map-to-xml.simple-xml :refer :all]))

(deftest padding-test
  (testing "padding"
    (is (= "" (padding 0)))
    (is (= "  " (padding 1)))
    (is (= "    " (padding 2)))
    (is (= "          " (padding 5)))
    (is (= "                " (padding 8)))
    ;; negative is the same as zero, no matter the magnitude
    (is (= "" (padding -1)))
    (is (= "" (padding -1000000000000000000)))
    ;; the intent is that the argument should be an integer;
    ;; but it works fine with any number
    (is (= "    " (padding 2.3)))
    ;; ... and, in fact, can give us an odd number of spaces
    (is (= "     " (padding 2.7)))
    ))
