(ns produkt.client
 (:require [produkt.ajax :as ajax]
           [goog.dom :as dom]
           [goog.dom.classes :as domclass]
           [goog.events.Event :as goog-event]
           [goog.events.EventType :as goog-event-type]
           [goog.ui.CustomButton :as custom-button]
           [goog.ui.Component.EventType :as comp-event-type]
           [goog.ui.Tab :as gtab]
           [goog.ui.TabBar :as gtabb]
           [goog.ui.RoundedTabRenderer :as grtabr]))

(def base-url "http://localhost:8080/produkt/")

(def opret-button (doto (goog.ui.CustomButton.)
                        (.setTooltip "Opret produkt")
                        (.setCaption "Opret")))

(def opret-meta-button (doto (goog.ui.CustomButton.)
                         (.setTooltip "Opret Metadata værdi")
                         (.setCaption "Opret Meta")))

(def opret-service-button (doto (goog.ui.CustomButton.)
                        (.setTooltip "Opret service")
                        (.setCaption "Opret S")))

(def opret-hw-button (doto (goog.ui.CustomButton.)
                        (.setTooltip "Opret hardware")
                        (.setCaption "Opret HW")))

(def tabbar (goog.ui.TabBar.))

(defn init-tabbar []
  (doall (map #(. tabbar (addChild (goog.ui.Tab. %) true)) ["Produkt" "Services" "Hardware"]))
  (do (. tabbar (setSelectedTabIndex 0))))

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

(defn hideall []
  (doall (map #(domclass/swap (dom/getElement %) "goog-tab-content" "hid") ["produkt_content" "services_content" "hardware_content"])))

(defn tab-select [tabbar e]
  (let [tab (.target e)]
    (hideall)
    (cond
     (= (. tab (getCaption)) "Produkt") (domclass/swap (dom/getElement "produkt_content") "hid" "goog-tab-content")
     (= (. tab (getCaption)) "Services") (domclass/swap (dom/getElement "services_content") "hid" "goog-tab-content")
     (= (. tab (getCaption)) "Hardware") (domclass/swap (dom/getElement "hardware_content") "hid" "goog-tab-content"))))

(defn ^:export main [] 
  ;(doajax)
  ;(init-tabbar)
  (.render opret-button (dom/getElement "submit_p"))
  (.render opret-meta-button (dom/getElement  "button_m"))
  (.render opret-service-button (dom/getElement  "submit_s"))
  (.render opret-hw-button (dom/getElement  "submit_hw"))
  (.decorate tabbar (dom/getElement "menu"))
  (.listen goog.events
           opret-button
           goog.ui.Component.EventType/ACTION
           event1)

  (.listen goog.events
           opret-hw-button
           goog.ui.Component.EventType/ACTION
           event1)

  (.listen goog.events
           opret-service-button
           goog.ui.Component.EventType/ACTION
           event1)
  
  (.listen goog.events
           opret-meta-button
           goog.ui.Component.EventType/ACTION
           event1)
  
  (.listen goog.events
           tabbar
           goog.ui.Component.EventType/SELECT
           (partial tab-select tabbar)))