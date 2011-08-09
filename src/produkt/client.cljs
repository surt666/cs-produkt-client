(ns produkt.client
 (:require [produkt.ajax :as ajax]
           [goog.dom :as dom]
           [goog.events.Event :as goog-event]
           [goog.events.EventType :as goog-event-type]
           [goog.ui.CustomButton :as custom-button]
           [goog.ui.Component.EventType :as comp-event-type]))

(def base-url "http://localhost:8080/produkt/")

(def opret-button (doto (goog.ui.CustomButton.)
                        (.setTooltip "Opret produkt")
                        (.setCaption "Opret")))

(def opret-meta-button (doto (goog.ui.CustomButton.)
                        (.setTooltip "Opret Metadata værdi")
                        (.setCaption "Opret Meta")))

(defn- generate-options [data]
  (reduce str (map #(str "<option id=\"" (:id %) "\">" (:navn %) "</option>") data)))

(defn- select-data [elem-id e]
 (let [json (. (aget e "target") (getResponseJson))
       data (js->clj json :keywordize-keys true) 
       elem (dom/getElement elem-id)
       options (dom/htmlToDocumentFragment (generate-options data))] 
   (dom/insertChildAt elem options 0)))

(defn doajax []
  (produkt.ajax/get-uri (str base-url "findalle/hardware") (partial select-data "hardware"))
  (produkt.ajax/get-uri (str base-url "findalle/services") (partial select-data "services"))
  (produkt.ajax/get-uri (str base-url "findalle/produkter") (partial select-data "produkter")))

(defn event1 []
  ((js* "alert") "BLA"))

(defn ^:export main [] 
 ;(doajax)
  (.render opret-button (dom/getElement  "submit_p"))
  (.listen goog.events
           opret-button
           goog.ui.Component.EventType/ACTION
           event1)
  (.render opret-meta-button (dom/getElement  "submit_m"))
  (.listen goog.events
           opret-meta-button
           goog.ui.Component.EventType/ACTION
           event1))