(defproject cljfmt "0.9.2"
  :description "A library for formatting Clojure code"
  :url "https://github.com/weavejester/cljfmt"
  :scm {:dir ".."}
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/tools.cli "1.0.214"]
                 [org.clojure/tools.reader "1.3.6"]
                 [com.googlecode.java-diff-utils/diffutils "1.3.0"]
                 [rewrite-clj "1.1.45"]]
  :plugins [[lein-cljsbuild "1.1.7"]
            [io.taylorwood/lein-native-image "0.3.1"]]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds
              {"dev" {:source-paths ["src" "test"]
                      :compiler {:main cljfmt.test-runner
                                 :output-to "target/out/tests.js"
                                 :output-dir "target/out"
                                 :target :nodejs
                                 :optimizations :none}}}
              :test-commands
              {"dev" ["node" "target/out/tests.js"]}}
  :native-image
  {:name "cljfmt"
   :opts ["--verbose"
          "-H:+ReportExceptionStackTraces"
          "-H:ReflectionConfigurationFiles=reflection.json"
          "--initialize-at-build-time"
          "--diagnostics-mode"
          "--report-unsupported-elements-at-runtime"
          "-H:Log=registerResource:"
          "--no-fallback"
          "-J-Xmx3g"]}
  :main cljfmt.main
  :profiles
  {:uberjar {:main cljfmt.main
             :aot :all}
   :native-image {:aot :all
                  :main cljfmt.main
                  :jvm-opts ["-Dclojure.compiler.direct-linking=true"
                             "-Dclojure.spec.skip-macros=true"]}
   :pgo-instrument {:native-image {:opts ["--pgo-instrument"]}}
   :pgo {:native-image {:opts ["--pgo=default.iprof"]}}
   :static {:native-image {:opts ["--static"]}}
   :no-gc {:native-image {:opts ["--gc=epsilon"]}}
   :provided {:dependencies [[org.clojure/clojurescript "1.11.4"]]}})
