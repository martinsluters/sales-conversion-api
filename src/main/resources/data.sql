INSERT INTO public.action_type (id, name, slug)
SELECT 1, 'Product viewed', 'product_viewed'
WHERE NOT EXISTS (SELECT id FROM public.action_type WHERE id = 1);

INSERT INTO public.action_type (id, name, slug)
SELECT 2, 'Product added to cart', 'product_added_cart'
WHERE NOT EXISTS (SELECT id FROM public.action_type WHERE id = 2);

INSERT INTO public.action_type (id, name, slug)
SELECT 3, 'Checkout started', 'checkout_started'
WHERE NOT EXISTS (SELECT id FROM public.action_type WHERE id = 3);

INSERT INTO public.action_type (id, name, slug)
SELECT 4, 'Purchase completed', 'purchase_completed'
WHERE NOT EXISTS (SELECT id FROM public.action_type WHERE id = 4);