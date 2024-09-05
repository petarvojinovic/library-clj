(ns controller.user_controller
  (:require [ring.util.response :as response]
            [compojure.core :refer [defroutes GET POST PUT DELETE]]
            [service.user_service :as user-service]
            [cheshire.core :as json]
            [validation.user_validation :as validation]))

(defn serialize-to-pretty-json [data]
  (json/generate-string data {:pretty true}))

(defroutes user-routes
           (GET "/user" []
             (let [users (user-service/get-all-users)
                   json-string (serialize-to-pretty-json users)]
               (response/response json-string)))

           (GET "/user/:id" [id]
             (let [user (user-service/get-user-by-id id)]
               (if (= :ok (:status user))
                 (response/response (serialize-to-pretty-json (:data user)))
                 (response/status (response/response (:message user)) 404))))

           (GET "/members" []
             (let [members (user-service/get-members)
                   json-string (serialize-to-pretty-json members)]
               (response/response json-string)))

           (POST "/user" request
             (let [json-parsed (:body request)
                   validation-result (validation/validate-user json-parsed)]
               (if (= :ok (:status validation-result))
                 (let [{:keys [username password name email phone role membership_type]} json-parsed
                       creation-result (user-service/create-user username password name email phone role membership_type)]
                   (if (= :ok (:status creation-result))
                     (response/response (serialize-to-pretty-json creation-result))
                     (response/status (response/response (serialize-to-pretty-json creation-result)) 400)))
                 (response/status (response/response (serialize-to-pretty-json validation-result)) 400))))

           (PUT "/user/:id" request
             (let [json-parsed (:body request)
                   user-id (-> request :params :id)
                   validation-result (validation/validate-user json-parsed)]
               (if (= :ok (:status validation-result))
                 (let [{:keys [username password name email phone role membership_type]} json-parsed
                       update-result (user-service/update-user user-id username password name email phone role membership_type)]
                   (if (= :ok (:status update-result))
                     (response/response (serialize-to-pretty-json update-result))
                     (response/status (response/response (serialize-to-pretty-json update-result)) 404)))
                 (response/status (response/response (serialize-to-pretty-json validation-result)) 400))))

           (DELETE "/user/:id" [id]
             (let [delete-result (user-service/delete-user id)]
               (if (= :ok (:status delete-result))
                 (response/response (serialize-to-pretty-json delete-result))
                 (response/status (response/response (serialize-to-pretty-json delete-result)) 404)))))


           ;(GET "/user/:id/recommendations" [id]
           ;  (let [recommendations (recommendation-service/recommend-books (Integer/parseInt id))
           ;        books-with-author (map (fn [book]
           ;                                 (assoc book :author {:author_id (:author_id book)
           ;                                                      :name (:author_name book)
           ;                                                      :birth_year (:author_birth_year book)}))
           ;                               recommendations)
           ;        books-cleaned (map #(dissoc % :author_name :author_birth_year :author_id) books-with-author)
           ;        json-string (serialize-to-pretty-json books-cleaned)]
           ;    (response/response json-string))))
