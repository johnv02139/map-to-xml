;   Copyright (c) John Valente. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns ^{:doc "Functions to emit a Clojure map as XML."
      :author "John Valente"}
    map-to-xml.simple-xml
  (:refer-clojure :exclude [contains?]))

;; This package is intended to write Clojure data as XML in a way that seems
;; natural and obvious -- at least, to me.
;;
;; If you have a map, {:foo "bar"}, I'd think the obvious way to write that
;; into XML would be: <foo>bar</foo>.
;;
;; Apparently, that is not so obvious.  I originally wrote this quick and dirty
;; code around 2014.  I first searched for an existing package, certain that a
;; good one must exist.  I found stuff to generate XML from Clojure, but they
;; all seemed to want the data to be annotated.  At best, given a nested map
;; like {:foo {:bar "hello"}}, they'd produce <foo bar="hello" />.  That's not
;; what I wanted.
;;
;; Now it's 2018 and the clojure.data.xml package has been created.  But it
;; does the same thing as the other packages did.  It also, by default,
;; produces unformatted, unreadable XML.  It has the ability to do indentation,
;; but warns that it is so slow that it should be considered a debugging
;; feature.
;;
;; I suppose one can conclude that clojure.data.xml is intended for use in
;; creating large amounts of fully-featured XML, using the Clojure language.
;; That's not what I want.  I want to represent Clojure data -- any Clojure
;; data, with an emphasis on what is most common -- as XML.
;;
;; This package has no dependencies, which is nice in some sense, but also
;; means I'm likely re-inventing the wheel a lot.  It might be nice to
;; integrate this with data.xml, so that it would take generic Clojure data,
;; massage it into the format necessary for data.xml to treat maps as tag
;; and content, and then pass it on to data.xml, specifying the "slow"
;; indentation be used.
;;
;; Or I could just make this better.

(def spaces-per-indent 2)

(defn padding [depth]
  (apply str (repeat (* spaces-per-indent depth) \space)))

(declare xml-element)

(def line-break "\n") ;; could use \newline character?
(def sequence-tag "elt")
(def xml-header "<?xml version='1.0' encoding='UTF-8'?>")

(defn xml-tag-name [requested-tag]
  (name requested-tag))

(defn xml-open-tag [tag-name]
  (str "<" (xml-tag-name tag-name) ">"))
   
(defn xml-close-tag [tag-name]
  (str "</" (xml-tag-name tag-name) ">"))

(defn xml-empty-tag [tag-name]
  (str "<" (xml-tag-name tag-name) " />"))
   
(defn xml-comment [depth & msgs]
  (str
   (padding depth)
   "<!-- "
   (apply str msgs)
   " -->"
   line-break))

(defn lisp-false [x]
  (or (not x)
      (and (sequential? x) (empty? x))))

(defn xml-key-value [k v depth]
  (if (lisp-false v)
    (xml-empty-tag k)
    (str
     (xml-open-tag k)
     line-break
     (xml-element (inc depth) v)
     line-break
     (padding depth)
     (xml-close-tag k))))

(defn xml-map-entry [depth [k v]]
  (str
   (padding depth)
   (xml-key-value k v depth)))

(defn xml-seq-element [depth x]
  (str
   (padding depth)
   (xml-open-tag sequence-tag)
   line-break
   (xml-element (inc depth) x)
   line-break
   (padding depth)
   (xml-close-tag sequence-tag)
   line-break))

(defn xml-element [depth x]
  (cond
    (or (string? x) (char? x) (number? x))
    (str (padding depth) x)

    (instance? String x)
    (str (padding depth) x)

    (instance? clojure.lang.Named x)
    (str (padding depth) (xml-tag-name x))

    (map? x)
    (->> x
         (map #(xml-map-entry depth %))
         (interpose line-break)
         (apply str))
    
    (sequential? x)
    (->> x
         (map #(xml-seq-element depth %))
         (interpose line-break)
         (apply str))

    :default
    (str
     (xml-comment
      depth
      "warning: unrecognized type: "
      (type x))
     (padding depth)
     (str x))))
    
(defn xml [x]
  (str
   xml-header
   line-break
   (xml-element 0 x)))
