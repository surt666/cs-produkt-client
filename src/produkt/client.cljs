(ns produkt.client
 (:require [produkt.ajax :as ajax]
           [goog.dom :as dom]))

(defn- generate-options [data]
  (reduce str (map #(str "<option id=\"" (:id %) "\">" (:navn %) "</option>") data)))

(defn- select-data [elem-id e]
 (let [json (. (aget e "target") (getResponseJson))
       data (js->clj json :keywordize-keys true) 
       elem (dom/getElement elem-id)
       options (dom/htmlToDocumentFragment (generate-options data))] 
   (dom/insertChildAt elem options 0)))

(defn doajax []
  (produkt.ajax/get-uri "http://localhost:8080/produkt/findalle/hardware" (partial select-data "hardware"))
  (produkt.ajax/get-uri "http://localhost:8080/produkt/findalle/services" (partial select-data "services"))
  (produkt.ajax/get-uri "http://localhost:8080/produkt/findalle/produkter" (partial select-data "produkter")))

(doajax)