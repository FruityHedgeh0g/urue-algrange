import { useQuery } from "@tanstack/react-query";
import { fetchPostById, fetchPosts } from "./postsApi";
import { queryKeys } from "../queryKeys";

export function usePosts() {
  return useQuery({ queryKey: queryKeys.posts.all, queryFn: fetchPosts });
}

export function usePost(postId: string | undefined) {
  return useQuery({
    queryKey: queryKeys.posts.detail(postId),
    queryFn: () => fetchPostById(postId as string),
    enabled: Boolean(postId),
  });
}
