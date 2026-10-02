import { mockPosts } from "./fixtures";
import { Post, PostAuthor, PostStatus } from "./types";
import { JsonStore, localJsonStore } from "../../lib/storage/jsonStore";
import { createOverlayCollection } from "../../lib/storage/overlayCollection";

/** Ce que le Bureau écrit ; statut et auteur sont fixés par l'API. */
export interface PostInput {
  title: string;
  content: string;
}

/**
 * Client mocké, mêmes contrats que PostController : GET /api/posts (+ /{postId})
 * ne renvoie les Brouillons qu'au Bureau (`seesDrafts`), POST crée un Brouillon
 * signé par la personne connectée, PATCH modifie, PUT /{postId}/status publie
 * (`publie`) ou repasse en brouillon (`brouillon`).
 */
export function createPostsApi(store: JsonStore = localJsonStore) {
  const posts = createOverlayCollection<Post>({ store, name: "post", fixtures: mockPosts, idOf: (p) => p.postId });
  const visible = (seesDrafts: boolean) => (post: Post) => seesDrafts || post.status === "publie";
  /** Un Post a un titre et un contenu. */
  const validate = ({ title, content }: PostInput) => {
    if (!title.trim()) throw new Error("Une actualité a un titre.");
    if (!content.trim()) throw new Error("Une actualité a un contenu.");
  };

  return {
    fetchPosts: async (seesDrafts: boolean): Promise<Post[]> => (await posts.list()).filter(visible(seesDrafts)),
    fetchPostById: async (postId: string, seesDrafts: boolean): Promise<Post | undefined> => {
      const post = await posts.get(postId);
      return post && visible(seesDrafts)(post) ? post : undefined;
    },
    createPost: async (input: PostInput, author: PostAuthor): Promise<Post> => {
      validate(input);
      const post: Post = { postId: `post-${Date.now()}-${Math.random().toString(36).slice(2, 7)}`, ...input, status: "brouillon", author };
      await posts.create(post);
      return post;
    },
    updatePost: async (postId: string, patch: PostInput) => {
      validate(patch);
      await posts.update(postId, patch);
    },
    changePostStatus: (postId: string, status: PostStatus) => posts.update(postId, { status }),
  };
}

export const { fetchPosts, fetchPostById, createPost, updatePost, changePostStatus } = createPostsApi();
