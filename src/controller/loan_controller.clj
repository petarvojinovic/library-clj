(ns controller.loan_controller
  (:require [ring.util.response :as response]
            [compojure.core :refer [defroutes GET POST PUT DELETE PATCH]]
            [service.loan_service :as loan-service]
            [cheshire.core :as json]
            [validation.loan_validation :as validation]))

(defn serialize-to-pretty-json [data]
  (json/generate-string data {:pretty true}))

(defroutes loan-routes
           (GET "/loan" []
             (let [loans (loan-service/get-all-loans)
                   json-string (serialize-to-pretty-json loans)]
               (response/response json-string)))

           (GET "/loan/:user_id/:book_id" [user_id book_id]
             (let [loan (loan-service/get-loan user_id book_id)]
               (if (= :ok (:status loan))
                 (response/response (serialize-to-pretty-json (:data loan)))
                 (response/status (response/response (:message loan)) 404))))

           (POST "/loan" request
             (let [json-parsed (:body request)
                   {:keys [user_id book_id loan_date return_date]} json-parsed
                   validation-result (validation/validate-loan json-parsed)]
               (if (= :ok (:status validation-result))
                 (let [creation-result (loan-service/create-loan user_id book_id loan_date return_date)]
                   (if (= :ok (:status creation-result))
                     (response/response (serialize-to-pretty-json creation-result))
                     (response/status (response/response (serialize-to-pretty-json creation-result)) 400)))
                 (response/status (response/response (serialize-to-pretty-json validation-result)) 400))))

           (PUT "/loan/:user_id/:book_id" request
             (let [json-parsed (:body request)
                   user-id (-> request :params :user_id)
                   book-id (-> request :params :book_id)
                   {:keys [loan_date return_date]} json-parsed
                   update-result (loan-service/update-loan user-id book-id loan_date return_date)]
               (if (= :ok (:status update-result))
                 (response/response (serialize-to-pretty-json update-result))
                 (response/status (response/response (serialize-to-pretty-json update-result)) 404))))

           (DELETE "/loan/:user_id/:book_id" [user_id book_id]
             (let [delete-result (loan-service/delete-loan user_id book_id)]
               (if (= :ok (:status delete-result))
                 (response/response (serialize-to-pretty-json delete-result))
                 (response/status (response/response (serialize-to-pretty-json delete-result)) 404))))

           (PATCH "/loan/update/:user_id/:book_id" [user_id book_id return_date]
                  (let [update-result (loan-service/change-loan-return-date user_id book_id return_date)]
                    (if (= :ok (:status update-result))
                      (response/response (serialize-to-pretty-json update-result))
                      (response/status (response/response (serialize-to-pretty-json update-result)) 404))))
           )
