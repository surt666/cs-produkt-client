(ns client
 (:require [goog.net.XhrIo :as xhrio]
           [goog.dom :as dom]))

(defn show [msg]
 (let [json (. (aget msg "target") (getResponseJson))
       data (js->clj json :keywordize-keys true)
       div (dom/getElement "test")]
   (dom/setTextContent div (:navn(first data)))))

(def uri (goog.Uri. "http://localhost:8080/produkt/findalle/hardware"))

(defn doajax2 []
  (goog.net.XhrIo/send uri show "GET"))

(doajax2)