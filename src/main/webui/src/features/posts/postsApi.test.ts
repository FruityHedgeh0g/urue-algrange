import { afterEach, describe, expect, it } from "vitest";
import { createPostsApi } from "./postsApi";

const AUTHOR = { userId: "bureau-1", firstName: "Claire", lastName: "Hoffmann" };

describe("postsApi: drafts and authors", () => {
  afterEach(() => localStorage.clear());

  it("creates a Brouillon with its creator as author, hidden from the public", async () => {
    const api = createPostsApi();
    const post = await api.createPost({ title: "Test brouillon", content: "Contenu" }, AUTHOR);
    expect(post).toMatchObject({ status: "brouillon", author: AUTHOR });

    expect((await api.fetchPosts(false)).map((p) => p.postId)).not.toContain(post.postId);
    expect(await api.fetchPostById(post.postId, false)).toBeUndefined();
    expect((await api.fetchPosts(true)).map((p) => p.postId)).toContain(post.postId);
  });

  it("publishes, edits and unpublishes", async () => {
    const api = createPostsApi();
    const post = await api.createPost({ title: "Test", content: "Contenu" }, AUTHOR);

    await api.changePostStatus(post.postId, "publie");
    expect((await api.fetchPostById(post.postId, false))?.status).toBe("publie");

    await api.updatePost(post.postId, { title: "Test renommé", content: "Contenu" });
    expect((await api.fetchPostById(post.postId, false))?.title).toBe("Test renommé");

    await api.changePostStatus(post.postId, "brouillon");
    expect(await api.fetchPostById(post.postId, false)).toBeUndefined();
  });
});
