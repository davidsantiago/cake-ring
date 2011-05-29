(ns cake-ring.internal
  "Some functions useful to include from the task within bake.")

;; Hold onto the server instance here.
(defonce *app* (atom nil))