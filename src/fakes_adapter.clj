(ns fakes-adapter
  (:use org.httpkit.fake)
  (:require [clojure.test :refer :all]
            [clj-http.fake :as clj-http-fake]))

(defn with-method [req res]
  (let [req-sym (gensym)]
    {(:url req)
     {(:method req)
      (with-meta
        `(fn [~req-sym]
           ~res)
        {:req-sym req-sym})}}))

(defn with-query-params [req res]
  (let [req-sym (gensym)]
    {{:address      (:url req)
      :query-params (:query-params req)}
     (with-meta
       `(fn [~req-sym]
          ~res)
       {:req-sym req-sym})}))

(defn convert-stub [httpkit-stub-vec]
  (let [req (first httpkit-stub-vec)
        res (second httpkit-stub-vec)]
    (if (:query-params req)
      (with-query-params req res)
      (with-method req res))))

(defn merge-stubs [stubs]
  (reduce merge (map convert-stub (partition 2 stubs))))

(defmacro http-fake-adapter
  "Evaluates http-kit stubs expressions and re-writes it into
  equivalent stub expressions for clj-http-fake"
  ([http-kit-stub]
   `(clj-http-fake/with-fake-routes-in-isolation
      ~(merge-stubs (eval (second http-kit-stub)))
      ~(nth http-kit-stub 2))))
