(ns controller.loan_controller
  (:require [ring.util.response :as response]
            [compojure.core :refer [defroutes GET POST PUT DELETE]]
            [service.loan_service :as loan-service]))

(defn convert-to-json-string [data]
  (clojure.core/pr-str data))

(defroutes loan-routes
           (GET "/loans" []
             (let [loans (loan-service/get-all-loans)
                   json-string (convert-to-json-string loans)]
               (response/response json-string)))

           (GET "/loans/:member_id/:book_id" [member_id book_id]
             (let [loan (loan-service/get-loan member_id book_id)
                   json-string (convert-to-json-string loan)]
               (response/response json-string)))

           (POST "/loans" request
             (let [json-parsed (:body request)]
               (let [{:keys [member_id book_id loan_date return_date]} json-parsed]
                 (loan-service/create-loan member_id book_id loan_date return_date)
                 (response/response "Loan created successfully"))))

           (PUT "/loans/:member_id/:book_id" request
             (let [json-parsed (:body request)
                   member-id (-> request :params :member_id)
                   book-id (-> request :params :book_id)]
               (let [{:keys [loan_date return_date]} json-parsed]
                 (loan-service/update-loan member-id book-id loan_date return_date)
                 (response/response "Loan updated successfully"))))

           (DELETE "/loans/:member_id/:book_id" [member_id book_id]
             (loan-service/delete-loan member_id book_id)
             (response/response "Loan deleted successfully")))
