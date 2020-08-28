(ns fakes-adapter-macro-test
  (:require
    [clojure.test :refer :all]
    [fakes-adapter :refer [http-fake-adapter]]
    [support.http-kit-api :as http-kit-stubs]))

(deftest test-fake-adapter
  (testing "it should convert http-kit.fake to clj-http.fake with single stub"
    (let [response-value (second
                           (http-kit-stubs/on-get
                             "base-url"
                             {:users {:href      "/users{?admin}"
                                      :templated true}}))
          out (macroexpand-1
                `(http-fake-adapter
                   (with-fake-http
                     ~(http-kit-stubs/on-get
                        "base-url"
                        {:users {:href      "/users{?admin}"
                                 :templated true}})

                     "hello-world")))

          req-symbol (:req-sym (meta (:get (get (second out) "base-url"))))]

      (is (= out
            `(clj-http.fake/with-fake-routes-in-isolation
               {"base-url" {:get (fn [~req-symbol] ~response-value)}}

               "hello-world")))))

  (testing "it should convert http-kit.fake to clj-http.fake with query-params"
    (let [response-value (second
                           (http-kit-stubs/on-get
                             "base-url"
                             {:param-one "one"
                              :param-two "two"}
                             {:users {:href      "/users{?admin}"
                                      :templated true}}))

          out (macroexpand-1
                `(http-fake-adapter
                   (with-fake-http
                     ~(http-kit-stubs/on-get
                       "base-url"
                       {:param-one "one"
                        :param-two "two"}
                       {:users {:href      "/users{?admin}"
                                :templated true}})

                     "hello-world")))

          req-symbol (:req-sym
                       (meta (get
                               (second out)
                               {:address      "base-url"
                                :query-params {:param-one "one" :param-two "two"}})))]

      (is (= out
            `(clj-http.fake/with-fake-routes-in-isolation
               {{:address      "base-url"
                 :query-params {:param-one "one"
                                :param-two "two"}}
                (fn [~req-symbol] ~response-value)}

               "hello-world")))))

  (testing "it should convert http-kit.fake to clj-http.fake with multple stubs"
    (let [response-value-1 (second (http-kit-stubs/on-get
                                     "base-url"
                                     {:users {:href      "/users{?admin}"
                                              :templated true}}))
          response-value-2 (second (http-kit-stubs/on-head
                                     "base-url-2"
                                     {:status 200}))

          out (macroexpand-1
                `(http-fake-adapter
                   (with-fake-http
                     (concat
                       ~(http-kit-stubs/on-get
                         "base-url"
                         {:users {:href      "/users{?admin}"
                                  :templated true}})
                       ~(http-kit-stubs/on-head
                         "base-url-2"
                         {:status 200}))
                     "hello world")))

          req-symbol-1 (:req-sym (meta (:get (get (second out) "base-url"))))
          req-symbol-2 (:req-sym (meta (:head (get (second out) "base-url-2"))))]

      (is (= out
            `(clj-http.fake/with-fake-routes-in-isolation
               {"base-url"   {:get (fn [~req-symbol-1] ~response-value-1)}
                "base-url-2" {:head (fn [~req-symbol-2] ~response-value-2)}}
               "hello world"))))))
