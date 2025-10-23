import type { Link } from "./Link";

interface PaginatedResponse {
  content: Link[];
  pageNumber: number;
  pageSize: number;
  totalPage: number;
  totalElements: number;
  empty: boolean;
}