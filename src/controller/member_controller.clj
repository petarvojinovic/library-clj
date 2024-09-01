(ns controller.member_controller
  (:require [ring.util.response :as response]
            [compojure.core :refer [defroutes GET POST PUT DELETE]]
            [service.member_service :as member-service]
            [cheshire.core :as json]
            [validation.member_validation :as validation]))

(defn serialize-to-pretty-json [data]
  (json/generate-string data {:pretty true}))

(defroutes member-routes
           (GET "/members" []
             (let [members (member-service/get-all-members)
                   json-string (serialize-to-pretty-json members)]
               (response/response json-string)))

           (GET "/members/:id" [id]
             (let [member (member-service/get-member-by-id id)
                   json-string (serialize-to-pretty-json member)]
               (response/response json-string)))

           (POST "/members" request
             (let [json-parsed (:body request)]
               (let [{:keys [name email phone membership_start membership_end]} json-parsed
                     validation-result (validation/validate-member json-parsed)]
                 (if (= :ok (:status validation-result))
                   (do
                     (member-service/create-member name email phone membership_start membership_end)
                     (response/response (serialize-to-pretty-json {:message "Member created successfully"})))
                   (response/status
                     (response/response (serialize-to-pretty-json validation-result)) 400)))))

           (PUT "/members/:id" request
             (let [json-parsed (:body request)
                   member-id (-> request :params :id)]
               (let [{:keys [name email phone membership_start membership_end]} json-parsed]
                 (member-service/update-member member-id name email phone membership_start membership_end)
                 (response/response "Member updated successfully"))))

           (DELETE "/members/:id" [id]
             (member-service/delete-member id)
             (response/response "Member deleted successfully")))
