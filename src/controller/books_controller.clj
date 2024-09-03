(ns controller.books_controller
  (:require [ring.util.response :as response]
            [compojure.core :refer [defroutes GET POST PUT DELETE]]
            [service.books_service :as books-service]
            [cheshire.core :as json]
            [validation.book_validation :as validation]))

(defn serialize-to-pretty-json [data]
  (json/generate-string data {:pretty true}))

(defroutes book-routes
           (GET "/books" []
             (let [books (books-service/get-all-books)
                   books-with-author (map (fn [book]
                                            (assoc book :author {:author_id (:author_id book)
                                                                 :name (:author_name book)
                                                                 :birth_year (:author_birth_year book)}))
                                          books)
                   books-cleaned (map #(dissoc % :author_name :author_birth_year :author_id) books-with-author)
                   json-string (serialize-to-pretty-json books-cleaned)]
               (response/response json-string)))

           (GET "/books/:id" [id]
             (let [book (first (books-service/get-book-by-id id))]
               (if book
                 (let [book-with-author (assoc book :author {:author_id (:author_id book)
                                                             :name (:author_name book)
                                                             :birth_year (:author_birth_year book)})
                       book-cleaned (dissoc book-with-author :author_name :author_birth_year :author_id)
                       json-string (serialize-to-pretty-json book-cleaned)]
                   (response/response json-string))
                 (response/status (response/response "Book not found") 404))))

           (GET "/books/author/:author_id" [author_id]
             (if (books-service/author-exists? author_id)
               (let [books (books-service/get-books-by-author-id author_id)]
                 (if (empty? books)
                   (response/status (response/response "No books found for this author") 404)
                   (let [books-with-authors (map (fn [book]
                                                   (assoc book :author {:author_id (:author_id book)
                                                                        :name (:author_name book)
                                                                        :birth_year (:author_birth_year book)}))
                                                 books)
                         books-cleaned (map #(dissoc % :author_name :author_birth_year :author_id) books-with-authors)
                         json-string (serialize-to-pretty-json books-cleaned)]
                     (response/response json-string))))
               (response/status (response/response "Author not found") 404)))


           (POST "/books" request
             (let [json-parsed (:body request)]
               (let [{:keys [title genre year_published author_id]} json-parsed
                     validation-result (validation/validate-book json-parsed)]
                 (if (= :ok (:status validation-result))
                   (do
                     (books-service/create-book title genre year_published author_id)
                     (response/response (serialize-to-pretty-json {:message "Book created successfully"})))
                   (response/status
                     (response/response (serialize-to-pretty-json validation-result)) 400)))))

           (PUT "/books/:id" request
             (let [json-parsed (:body request)
                   book-id (-> request :params :id)]
               (let [{:keys [title genre year_published author_id]} json-parsed]
                 (books-service/update-book book-id title genre year_published author_id)
                 (response/response "Book updated successfully"))))

           (DELETE "/books/:id" [id]
             (books-service/delete-book id)
             (response/response "Book deleted successfully")))
