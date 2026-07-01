import { useQuery } from "@tanstack/react-query";
import { fetchPostById, fetchPosts } from "./postsApi";

export function usePosts() {
  return useQuery({ queryKey: ["posts"], queryFn: fetchPosts });
}

export function usePost(postId: string | undefined) {
  return useQuery({
    queryKey: ["posts", postId],
    queryFn: () => fetchPostById(postId as string),
    enabled: Boolean(postId),
  });
}
