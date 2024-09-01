(ns controller.member_controller
  (:require [ring.util.response :as response]
            [compojure.core :refer [defroutes GET POST PUT DELETE]]
            [service.member_service :as member-service]))

(defn convert-to-json-string [data]
  (clojure.core/pr-str data))

(defroutes member-routes
           (GET "/members" []
             (let [members (member-service/get-all-members)
                   json-string (convert-to-json-string members)]
               (response/response json-string)))

           (GET "/members/:id" [id]
             (let [member (member-service/get-member-by-id id)
                   json-string (convert-to-json-string member)]
               (response/response json-string)))

           (POST "/members" request
             (let [json-parsed (:body request)]
               (let [{:keys [name email phone membership_start membership_end]} json-parsed]
                 (member-service/create-member name email phone membership_start membership_end)
                 (response/response "Member created successfully"))))

           (PUT "/members/:id" request
             (let [json-parsed (:body request)
                   member-id (-> request :params :id)]
               (let [{:keys [name email phone membership_start membership_end]} json-parsed]
                 (member-service/update-member member-id name email phone membership_start membership_end)
                 (response/response "Member updated successfully"))))

           (DELETE "/members/:id" [id]
             (member-service/delete-member id)
             (response/response "Member deleted successfully")))
