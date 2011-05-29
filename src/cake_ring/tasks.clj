(ns cake-ring.tasks
  (:use cake cake.core
        cake-ring.internal))

(deftask ring #{}
  "Control a Ring development server."
  {args :ring}
  (bake (:use [bake.core :only [log print-stacktrace]]
              ring.adapter.jetty
              ring.util.servlet
              [ring.middleware stacktrace reload-modified]
              cake-ring.internal)
        [[cmd & opts :as task-args] args]
        (let [handler (get-in *project* [:ring :handler])
              init    (get-in *project* [:ring :init])
              destroy (get-in *project* [:ring :destroy])
              ring-options (get-in *project* [:ring :options])
              namespaces (filter (complement nil?)
                                 (map #(if (not (nil? %))
                                         (symbol (namespace %)))
                                      [handler init destroy]))]
          (when (not (empty? namespaces))
            (apply require namespaces)) ;; Calling require with 0 args kills us.
          (cond (= cmd "server")
                (do (if (not (nil? @*app*))
                      (.stop @*app*))
                    (let [handler-fn (ns-resolve 'user handler)
                          port (if (first opts)
                                 (Integer/parseInt (first opts))
                                 8080)
                          wrapped-handler
                          (-> handler-fn
                              (wrap-stacktrace)
                              (wrap-reload-modified
                               (if (:source-path *project*)
                                 [(:source-path *project*)]
                                 ["src/"])))]
                      (reset! *app* (run-jetty wrapped-handler
                                               (merge {:join? false
                                                       :port port}
                                                      ring-options)))
                      (if-let [init-fn (ns-resolve 'user init)]
                        (init-fn))
                      (log "Started Ring development server.")))
                (= cmd "stop")
                (do (if-let [destroy-fn (ns-resolve 'user destroy)]
                      (destroy-fn))
                    (.stop @*app*)
                    (log "Stopped Ring development server."))
                :else
                (log "Error: No ring command specified.")))))