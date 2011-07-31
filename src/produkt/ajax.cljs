(ns produkt.ajax
 (:require [goog.net.XhrIo :as xhrio]))

(defn get-uri [uri callback]
  (goog.net.XhrIo/send uri callback "GET"))