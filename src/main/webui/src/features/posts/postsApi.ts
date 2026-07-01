import { mockPosts } from "./fixtures";
import { Post } from "./types";

/** Client mocké — même signature qu'un futur GET /api/posts (+ /api/posts/{id}). */
export async function fetchPosts(): Promise<Post[]> {
  return Promise.resolve(mockPosts);
}

export async function fetchPostById(postId: string): Promise<Post | undefined> {
  return Promise.resolve(mockPosts.find((p) => p.postId === postId));
}
