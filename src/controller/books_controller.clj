(ns controller.books_controller
  (:require [ring.util.response :as response]
            [compojure.core :refer [defroutes GET POST PUT DELETE]]
            [service.books_service :as books-service]))

(defn convert-to-json-string [data]
  (clojure.core/pr-str data))

(defroutes book-routes
           (GET "/books" []
             (let [books (books-service/get-all-books)
                   json-string (convert-to-json-string books)]
               (response/response json-string)))

           (GET "/books/:id" [id]
             (let [book (books-service/get-book-by-id id)
                   json-string (convert-to-json-string book)]
               (response/response json-string)))

           (GET "/books/author/:author_id" [author_id]
             (let [books (books-service/get-books-by-author-id author_id)
                   json-string (convert-to-json-string books)]
               (response/response json-string)))

           (POST "/books" request
             (let [json-parsed (:body request)]
               (let [{:keys [title genre year_published author_id]} json-parsed]
                 (books-service/create-book title genre year_published author_id)
                 (response/response "Book created successfully"))))

           (PUT "/books/:id" request
             (let [json-parsed (:body request)
                   book-id (-> request :params :id)]
               (let [{:keys [title genre year_published author_id]} json-parsed]
                 (books-service/update-book book-id title genre year_published author_id)
                 (response/response "Book updated successfully"))))

           (DELETE "/books/:id" [id]
             (books-service/delete-book id)
             (response/response "Book deleted successfully")))
