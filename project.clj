(defproject dda/dda-webmeeting "0.1.0-SNAPSHOT"
  :description "Module for installing Jitsi or big blue button on top of a single host kubernetes cluster"
  :url "https://www.domaindrivenarchitecture.org"
  :license {:name "Apache License, Version 2.0"
            :url "https://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [[dda/dda-pallet "3.1.1"]
                 [dda/dda-k8s-crate "0.1.5"]]
  :target-path "target/%s/"
  :source-paths ["main/src"]
  :resource-paths ["main/resources"]
  :repositories [["snapshots" :clojars]
                 ["releases" :clojars]]
  :deploy-repositories [["snapshots" :clojars]
                        ["releases" :clojars]]
  :profiles {:dev {:source-paths ["test/src"
                                  "uberjar/src"]
                   :resource-paths ["test/resources"]
                   :dependencies
                   [[org.clojure/test.check "1.0.0"]
                    [dda/data-test "0.1.1"]
                    [dda/pallet "0.9.1" :classifier "tests"]
                    [ch.qos.logback/logback-classic "1.3.0-alpha5"]
                    [org.slf4j/jcl-over-slf4j "2.0.0-alpha1"]]
                   :leiningen/reply
                   {:dependencies [[org.slf4j/jcl-over-slf4j "1.8.0-beta0"]]
                    :exclusions [commons-logging]}}
             :test {:test-paths ["test/src"]
                    :resource-paths ["test/resources"]
                    :dependencies [[dda/pallet "0.9.1" :classifier "tests"]]}
             :uberjar {:source-paths ["uberjar/src"]
                       :resource-paths ["uberjar/resources"]
                       :aot :all
                       :main dda.pallet.webmeeting.main
                       :uberjar-name "webmeeting-standalone.jar"
                       :dependencies [[org.clojure/tools.cli "1.0.194"]
                                      [ch.qos.logback/logback-classic "1.3.0-alpha5"
                                       :exclusions [com.sun.mail/javax.mail]]
                                      [org.slf4j/jcl-over-slf4j "2.0.0-alpha1"]]}}
  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag"]
                  ["deploy"]
                  ["uberjar"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]
                  ["vcs" "push"]]
  :local-repo-classpath true)
