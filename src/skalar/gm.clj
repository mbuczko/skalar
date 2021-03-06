(ns skalar.gm)

(defn options
  [opts]
  (reduce
   (fn [reduced [k v]]
     (if (or (and (boolean? v) (not v))
             (= :output-directory k))
       reduced
       (str reduced " -" (name k) " " (when (string? v) v))))
   ""
   opts))

(defn crop
  ([coords]
   (when (vector? coords)
     (apply crop coords)))
  ([x y width height]
   ["crop" (str width "x" height "+" x "+" y)]))

(defn resize
  [size & opts]
  (let [resize-opts (set opts)]
    ["resize" (str size
                   (when (:exact resize-opts) "!")
                   (when (:keep-ratio resize-opts) "^")
                   (when (:no-profiles resize-opts) " +profile \"*\"")

                   ;; give a hint to the JPEG decoder that the image is going to be downscaled
                   ;; allowing it to run faster by avoiding returning full-resolution images to
                   ;; GraphicsMagick for the subsequent resizing operation: http://www.graphicsmagick.org/convert.html

                   " -size " size)]))

(defn thumbnail
  [size & opts]
  (let [resize-opts (set opts)]
    ["thumbnail" (str size
                      (when (:exact resize-opts) "!")
                      (when (:keep-ratio resize-opts) "^")
                      " +profile \"*\"")]))
