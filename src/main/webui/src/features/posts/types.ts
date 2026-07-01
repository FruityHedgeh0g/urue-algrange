import { Media } from "../medias/types";

/** Reflète PostDto côté backend (vue Detailed). */
export interface Post {
  postId: string;
  title: string;
  content: string;
  banner?: Media;
  attachments?: Media[];
}
