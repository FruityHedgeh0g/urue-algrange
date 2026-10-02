import React from "react";
import { useAllPosts, usePostMutations } from "../../features/posts/usePosts";
import { PostInput } from "../../features/posts/postsApi";
import { POST_STATUS_LABELS } from "../../features/posts/types";
import AdminCrudList from "../../components/organisms/AdminCrudList/AdminCrudList";
import FormField from "../../components/molecules/FormField/FormField";
import Button from "../../components/atoms/Button/Button";
import Spinner from "../../components/atoms/Spinner/Spinner";

const emptyDraft: PostInput = { title: "", content: "" };

/**
 * Actualités (Bureau) : chaque Post avec son statut et son auteur. Un nouveau
 * Post est un Brouillon signé par la personne connectée ; « Publier » le rend
 * visible de tous, « Repasser en brouillon » le retire.
 */
export const PostsAdminPage: React.FC = () => {
  const { data: posts, isLoading } = useAllPosts();
  const { create, update, changeStatus } = usePostMutations();

  if (isLoading) return <Spinner label="Chargement des actualités..." />;

  return (
    <AdminCrudList
      title="Actualités"
      hint="Une actualité est écrite en brouillon, visible du seul Bureau, puis publiée pour tous."
      items={posts ?? []}
      idOf={(p) => p.postId}
      display={(p) => ({
        title: p.title,
        subtitle: [POST_STATUS_LABELS[p.status], p.author && `${p.author.firstName} ${p.author.lastName}`].filter(Boolean).join(" · "),
      })}
      toDraft={(p): PostInput => ({ title: p.title, content: p.content })}
      renderFields={(draft, setDraft, post) => (
        <>
          <FormField label="Titre" value={draft.title} onChange={(e) => setDraft({ ...draft, title: e.target.value })} required />
          <FormField
            label="Contenu"
            multiline
            rows={6}
            value={draft.content}
            onChange={(e) => setDraft({ ...draft, content: e.target.value })}
            required
          />
          {post && (
            <Button
              type="button"
              variant="outline"
              label={post.status === "publie" ? "Repasser en brouillon" : "Publier"}
              disabled={changeStatus.isPending || update.isPending}
              onClick={async () => {
                // Ce qui vient d'être saisi est enregistré avant de publier ou dépublier
                await update.mutateAsync({ postId: post.postId, ...draft });
                changeStatus.mutate({ postId: post.postId, status: post.status === "publie" ? "brouillon" : "publie" });
              }}
            />
          )}
        </>
      )}
      onUpdate={(postId, draft) => update.mutateAsync({ postId, ...draft })}
      create={{
        buttonLabel: "+ Nouvelle actualité",
        submitLabel: "Créer le brouillon",
        emptyDraft,
        onCreate: (draft) => create.mutateAsync(draft),
      }}
    />
  );
};

export default PostsAdminPage;
