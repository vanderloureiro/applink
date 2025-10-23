import type { Link } from "./Link";

export interface PaginatedResponse {
  content: Link[];
  pageNumber: number;
  pageSize: number;
  totalPage: number;
  totalElements: number;
  empty: boolean;
}