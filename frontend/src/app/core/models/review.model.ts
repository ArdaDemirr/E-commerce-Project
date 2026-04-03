export interface MyReviewDTO {
  id: number;
  productId: number;
  productName: string;
  rating: number;
  comment: string;
  createdAt: string;
  helpfulVotes: number;
  sentiment: 'positive' | 'neutral' | 'negative';
}

export interface ProductReviewDTO {
  id: number;
  reviewerName: string;
  rating: number;
  comment: string;
  createdAt: string;
  helpfulVotes: number;
  sentiment: 'positive' | 'neutral' | 'negative';
}

export interface ReviewRequest {
  productId: number;
  rating: number;
  comment: string;
}
