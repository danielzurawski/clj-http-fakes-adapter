(defproject clj-http-fakes-adapter "0.0.1"
  :description "Macros for httpkit.fake to clj-http.fake conversion"
  :license {:name "BSD 3-Clause License"
            :url  "https://opensource.org/licenses/BSD-3-Clause"}
  :url "https://github.com/danielzurawski/clj-http-fakes-adapter"
  :dependencies [[org.clojure/clojure "1.9.0-alpha16"]
                 [http-kit.fake "0.2.2"]
                 [http-kit "2.3.0"]
                 [clj-http "3.10.2"]
                 [cheshire "5.10.0"]
                 [clj-http-fake "1.0.3"]]
  :plugins [[lein-eftest "0.5.2"]]
  :profiles {:shared {:dependencies [[nrepl "0.6.0"]]}
             :test   [:shared {:dependencies [[http-kit.fake "0.2.2"]
                                              [clj-http-fake "1.0.3"]
                                              [eftest "0.5.2"]]}]}
  :eftest {:multithread? false})
