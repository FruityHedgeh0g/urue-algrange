import React from "react";
import { Link, useParams } from "react-router-dom";
import { usePost } from "../../features/posts/usePosts";
import PageHero from "../../components/organisms/PageHero/PageHero";
import Spinner from "../../components/atoms/Spinner/Spinner";
import Icon from "../../components/atoms/Icon/Icon";
import styles from "./NewsDetailPage.module.css";

export const NewsDetailPage: React.FC = () => {
  const { postId } = useParams<{ postId: string }>();
  const { data: post, isLoading, isError } = usePost(postId);

  const back = (
    <Link className={styles.back} to="/actualites">
      <Icon name="arrowLeft" size={16} strokeWidth={2.5} /> Retour aux actualités
    </Link>
  );

  if (!post) {
    return (
      <div className="container">
        {back}
        {isLoading && <Spinner label="Chargement de l'actualité..." />}
        {isError && <p className={styles.error}>Impossible de charger cette actualité.</p>}
        {!isLoading && !isError && <p className={styles.error}>Cette actualité n'existe pas.</p>}
      </div>
    );
  }

  return (
    <article>
      <PageHero eyebrow="Actualité" title={post.title} imageSrc={post.banner?.url} />
      <div className={styles.body}>
        {back}
        {post.banner && <img className={styles.banner} src={post.banner.url} alt={post.banner.alt} />}
        <p className={styles.content}>{post.content}</p>
      </div>
    </article>
  );
};

export default NewsDetailPage;
