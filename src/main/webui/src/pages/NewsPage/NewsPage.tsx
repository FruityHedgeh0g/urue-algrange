import React from "react";
import PostList from "../../components/organisms/PostList/PostList";

export const NewsPage: React.FC = () => (
  <div className="container">
    <h1>Actualités</h1>
    <p>Suivez la vie de l'association : événements passés, actions de terrain et remises de dons.</p>
    <PostList />
  </div>
);

export default NewsPage;
