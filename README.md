# clj-http-fakes-adapter
Convert `http-kit.fake` to `clj-http.fake` at compile time using the `http-fake-adapter` macro.

The purpose of this macro is to help switch a project from `httpkit` to `clj-http` without having to re-write the tests from scratch.  


This library contains a macro `clj-http-fake-adapter` that can be used to convert an `httpkit.fake` fake to a `clj-http.fake` 

### Usages
The `http-fake-adapter` macro can wrap around a `http-kit.fake`:

```clojure
(http-fake-adapter
  (with-fake-http
    [{:method       :get
      :url          "http://localhost:8080"
      :query-params {:param-one 1
                     :param-two 2}}
      {:status 200 :body ""}]
    (test-code-goes-here)))
```
in order to convert it (at compile time) into an equivalent `clj-http.fake`:
```clojure
(with-fake-routes-in-isolation
  {{:address "http://localhost:8080"
    :query-params {:param-one 1
                   :param-two 2}}
    (fn [req] {:status 200 :body ""})}  

 (test-code-goes-here))
``` 
which works with clj-http.
