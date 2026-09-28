import React from "react";
import PostList from "../../components/organisms/PostList/PostList";
import PageHero from "../../components/organisms/PageHero/PageHero";

export const NewsPage: React.FC = () => (
  <>
    <PageHero
      eyebrow="Notre actualité"
      title="Actualités"
      lead="Suivez la vie de l'association : événements passés, actions de terrain et remises de dons."
    />
    <div className="container">
      <PostList />
    </div>
  </>
);

export default NewsPage;
