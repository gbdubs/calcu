#!/usr/bin/python

# import modules used here
import sys
import urllib2
import xml.etree.ElementTree as ET
import pdb
import uuid
from json import dump, load

# Gather our code in a main() function
def main():
	DATA_FILENAME = str(uuid.uuid4()) + ".txt"
	all_practice_problems = []
	benched =["http://www.exampleproblems.com/wiki/index.php/Abstract_Algebra", 'http://www.exampleproblems.com/wiki/index.php/PDE:Laplaces_Equation']
	practice_problem_urls = ['http://www.exampleproblems.com/wiki/index.php/PDE:Fourier_Transforms', 'http://www.exampleproblems.com/wiki/index.php/PDE:Laplace_Transforms', 'http://www.exampleproblems.com/wiki/index.php/PDE:Mathematical_Modeling', 'http://www.exampleproblems.com/wiki/index.php/Ordinary_Differential_Equations', 'http://www.exampleproblems.com/wiki/index.php/Fourier_Series', 'http://www.exampleproblems.com/wiki/index.php/Functional_Analysis', 'http://www.exampleproblems.com/wiki/index.php/Complex_Variables', 'http://www.exampleproblems.com/wiki/index.php/Integral_Equations', 'http://www.exampleproblems.com/wiki/index.php/Number_Theory', 'http://www.exampleproblems.com/wiki/index.php/Fraction_(mathematics)','http://www.exampleproblems.com/wiki/index.php/Linear_Algebra', 'http://www.exampleproblems.com/wiki/index.php/Geometry', 'http://www.exampleproblems.com/wiki/index.php/Calculus', 'http://www.exampleproblems.com/wiki/index.php/Multivariable_Calculus', 'http://www.exampleproblems.com/wiki/index.php/Calculus_of_Variations', 'http://www.exampleproblems.com/wiki/index.php/PDE:Method_of_characteristics', 'http://www.exampleproblems.com/wiki/index.php/PDE:Integration_and_Separation_of_Variables']
	find_number = 1
	for p_p_url in practice_problem_urls:
		response = urllib2.urlopen(p_p_url)
		html = response.read()
		
		primary_tag = p_p_url[find_closest_tag_before_point(p_p_url, "/", len(p_p_url), len(p_p_url)) + 1:].replace("_"," ").replace("#"," ").replace(":"," ")
		tags = primary_tag
		for s in primary_tag.split(' '):
			tags = tags + ',' + s
		
		first_find = html.find("solution</a>")
		while first_find > 0:
			solution_link = find_link(html, first_find)
			solution_url = "http://www.exampleproblems.com" + solution_link
			problem = find_problem_body(html, first_find + 13)
			cleaned = clean_to_latex(problem)
			solution = get_solution_from_url(solution_link)
			
			results = {
					'tags': tags,
					'solutionLink':solution_url, 
					'site':"www.exampleproblems.com", 
					'title':primary_tag + " Practice",
					'problem':cleaned,  
					'solution':solution}
			all_practice_problems.append(results)
			with open(DATA_FILENAME, mode='w') as feedsjson:
				dump(all_practice_problems, feedsjson, indent=4)
				feedsjson.close()
			
			find_number = find_number + 1
		
			print 'Practice Problem Number ' + str(find_number) + ", currently in " + primary_tag
			
			first_find = html.find("solution</a>", first_find + len(problem))		
	
def get_solution_from_url(url):
	if url[0] is "/":
		url = "http://www.exampleproblems.com" + url
	try:

		response = urllib2.urlopen(url)
		html = response.read()
		content_text_start = html.find('<div id="mw-content-text"')
		content_text_end = html.find('</div>', content_text_start)
		content = html[content_text_start: content_text_end + 6]
		
		parsed_content = content[content.find('>')+1:len(content) - 6]
		to_not_include = content.find('<a href="/wiki/index.php/Main_Page"')
		if to_not_include > 0:
			last_index_to_include = find_closest_tag_before_point(content, "<p>", to_not_include, to_not_include)
			parsed_content = content[content.find('>')+1:last_index_to_include]			
		
		return clean_to_latex(parsed_content)
		
	except urllib2.HTTPError:
		print "Solution not found: " + url
		return None
	except urllib2.URLError:
		print "Solution not found (II): " + url	
		return None


def find_closest_tag_before_point(text, tag, index, original_index):
	result = text.find(tag, index)
	if result < original_index and result > 0:
		return result
	else:
		return find_closest_tag_before_point(text, tag, index - 5, original_index)


def find_link(html, first_find):
	search_number = 1
	link_start_index = -1
	found = False
	while not found:
		link_start_index = html.find('href="', first_find - 5 * search_number) + 6
		if link_start_index < first_find:
			found = True
		search_number = search_number + 1
	link_end_index = html.find('"', link_start_index)
	return html[link_start_index : link_end_index]
	
def find_problem_body(html, start_index):
	next_br = html.find("<br>", start_index)
	next_brc = html.find("<br />", start_index)
	next_brt = html.find("<br/>", start_index)
	next_pa = html.find("</p>", start_index)
	next_a = html.find("<a href=", start_index)
	if next_br < 0:
		next_br = len(html)
	if next_brc < 0:
		next_brc = len(html)
	if next_brt < 0:
		next_brt = len(html)
	if next_pa < 0:
		next_pa = len(html)
	if next_a < 0:
		next_a = len(html)
		
	end_index = min(next_br, next_pa, next_a, next_brc, next_brt)
	return html[start_index: end_index]
	
def clean_to_latex(s):
	p = s + ""
	opening_tag = s.find('<img class="mwe-math-fallback')
	while opening_tag >= 0:
		closing_tag = s.find('>', opening_tag)
		img_tag = s[opening_tag:closing_tag + 1]
		alt_text_start = s.find('alt="', opening_tag, closing_tag) + 5
		if alt_text_start > 5:
			alt_text_end = s.find('"', alt_text_start)
			alt_text = " \( " +  s[alt_text_start:alt_text_end] + " \) "
			p = p.replace(img_tag, alt_text)
		opening_tag = s.find('<img class="mwe-math-fallback', closing_tag)
	return p
	
def get_practice_problem_urls():
	starting_page = "http://www.exampleproblems.com/wiki/index.php/Main_Page"
	crawled_pages = []
	practice_problems = []
	pages_to_crawl = [starting_page]
	base_url = "http://www.exampleproblems.com"
	
	while pages_to_crawl:
		url = pages_to_crawl.pop(0)
		print "Traversing: " + url + " queue length: " + str(len(pages_to_crawl)) + " practice problem pages gleaned: " + str(len(practice_problems))
		try:
			response = urllib2.urlopen(url)
			html = response.read()
			if html.find('<img class="mwe-math-fallback-png-inline tex" alt="') > -1:
				print('    Marked As PracticeProblemPage')
				if url not in practice_problems:
					practice_problems.append(url)
			
			index = html.find('id="mw-content-text"')
			
			while index < len(html) and html.find('<a href="', index) > 0:
				link_tag = html.find('<a href="', index)
				if link_tag > 0:
					end_link = html.find('"', index + 11)
					
					new_url = html[link_tag + 9: end_link]
					if acceptable_sublink(new_url):
						new_url = base_url + new_url
						if new_url not in pages_to_crawl and new_url not in crawled_pages:
							print "Added new url to crawl: " + new_url
							pages_to_crawl.append(new_url)
					index = end_link
					
			crawled_pages.append(url)
		except urllib2.HTTPError:
			print "Page not found: " + url
		except urllib2.URLError:
			print "Page not found (II): " + url	
	
	print "DONE TRAVERSING"
	print "DONE TRAVERSING"
	print "DONE TRAVERSING"
	
	print practice_problems
	return practice_problems
	
def acceptable_sublink(link):
	if len(link) < 5:
		return False
	if link[0] is not "/":
		return False
	if "title" in link:
		return False
	if ";" in link:
		return False
	if '&' in link:
		return False
	if "Physics" in link:
		return False
	if "File" in link:
		return False
	return True

def getUrlsFromFile():
	url = "http://www.exampleproblems.com"
	tree = ET.parse('typesOfProblems.html')
	root = tree.getroot()
	all_pages = []
	for child in root:
		for link in child:
			all_pages.append(url + link.attrib["href"])
	return all_pages


# Standard boilerplate to call the main() function to begin
# the program.
if __name__ == '__main__':
  main()