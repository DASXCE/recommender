(defproject recommender "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :source-paths ["src/clj" "src/cljs"]
  :test-paths ["test/clj"]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [ring-server "0.3.1"]
                 [com.datomic/datomic-free "0.9.4699"]
                 [liberator "0.11.0"]
                 [cheshire "5.3.1"]
                 [lib-noir "0.8.3"]
                 [org.clojure/clojurescript "0.0-2227"]
                 [domina "1.0.3-SNAPSHOT"]
                 [hiccups "0.2.0"]
                 [org.clojars.magomimmo/shoreleave-remote-ring "0.3.1-SNAPSHOT"]
                 [org.clojars.magomimmo/shoreleave-remote "0.3.1-SNAPSHOT"]
                 [com.cemerick/valip "0.3.2"]
                 [enlive "1.1.4"]
                 [cljs-ajax "0.2.6"]]
  :plugins [[lein-ring "0.8.10"]
            [lein-cljsbuild "1.0.3"]]
  :ring {:handler recommender.handler/app
         :init recommender.handler/init
         :destroy recommender.handler/destroy}
  :hooks [leiningen.cljsbuild]
  ;; cljsbuild options configuration
  :cljsbuild {:crossovers [recommender.utils.validation valip.core valip.predicates]
              :builds
              {:dev
               {;; CLJS source code path
                :source-paths ["src/cljs"]

                ;; Google Closure (CLS) options configuration
                :compiler {;; CLS generated JS script filename
                           :output-to "resources/public/js/dev.js"

                           ;; minimal JS optimization directive
                           :optimizations :whitespace

                           ;; generated JS code prettyfication
                           :pretty-print true}}}}
  :console [(:lt.objs.console/set-console-limit 5000)]
  :aot :all
  :profiles
  {:production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.2.1"]]}})
