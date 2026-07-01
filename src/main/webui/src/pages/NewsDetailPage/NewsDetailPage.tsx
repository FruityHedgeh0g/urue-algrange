import React from "react";
import { Link, useParams } from "react-router-dom";
import { usePost } from "../../features/posts/usePosts";
import Spinner from "../../components/atoms/Spinner/Spinner";
import styles from "./NewsDetailPage.module.css";

export const NewsDetailPage: React.FC = () => {
  const { postId } = useParams<{ postId: string }>();
  const { data: post, isLoading, isError } = usePost(postId);

  return (
    <div className="container">
      <Link className={styles.back} to="/actualites">
        ← Retour aux actualités
      </Link>

      {isLoading && <Spinner label="Chargement de l'actualité..." />}
      {isError && <p className={styles.error}>Impossible de charger cette actualité.</p>}
      {!isLoading && !isError && !post && <p className={styles.error}>Cette actualité n'existe pas.</p>}

      {post && (
        <article>
          {post.banner && <img className={styles.banner} src={post.banner.url} alt={post.banner.alt} />}
          <h1>{post.title}</h1>
          <p className={styles.content}>{post.content}</p>
        </article>
      )}
    </div>
  );
};

export default NewsDetailPage;
