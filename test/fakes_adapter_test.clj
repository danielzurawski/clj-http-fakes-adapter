(ns fakes-adapter-test
  (:use org.httpkit.fake)
  (:require
    [clojure.test :refer :all]
    [fakes-adapter :refer [http-fake-adapter]]
    [clj-http.client :as http]))

(deftest test-fake-adapter
  (testing "it should intercept clj-http request using http-kit.fake stub
    wrapped in http-fake-adapter"
    (http-fake-adapter
      (with-fake-http [{:url "http://foo.co/" :method :get}
                       {:status 200 :body "ok"}]
        (let [response (http/get "http://foo.co/")]
          (is (= (:status response) 200))))))

  (testing "it should intercept clj-http request with query parameters
    using http-kit.fake stub wrapped in http-fake-adapter"
    (http-fake-adapter
      (with-fake-http [{:method :get
                        :url "http://foo.co/"
                        :query-params {:param-one "1"}}
                       {:status 200 :body "ok"}]
        (let [response (http/get "http://foo.co/" {:query-params {:param-one "1"}})]
          (is (= (:status response) 200)))))))
