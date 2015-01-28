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
	benched =[
		"http://www.exampleproblems.com/wiki/index.php/Abstract_Algebra", 
		'http://www.exampleproblems.com/wiki/index.php/PDE:Laplaces_Equation'
	]
	practice_problem_urls = [
		'http://www.exampleproblems.com/wiki/index.php/Trigonometry',
		'http://www.exampleproblems.com/wiki/index.php/Calculus', 
		'http://www.exampleproblems.com/wiki/index.php/Integral_Equations', 
		'http://www.exampleproblems.com/wiki/index.php/Multivariable_Calculus', 
		'http://www.exampleproblems.com/wiki/index.php/Calculus_of_Variations', 
		
		'http://www.exampleproblems.com/wiki/index.php/Algebra-Exponents',
		'http://www.exampleproblems.com/wiki/index.php/Algebra-Radicals',
		'http://www.exampleproblems.com/wiki/index.php/Algebra-Equations',
		'http://www.exampleproblems.com/wiki/index.php/Algebra-ExponentialAndLogarithmicEquations',
		'http://www.exampleproblems.com/wiki/index.php/Algebra-Partial_Fraction_Decomposition',
		'http://www.exampleproblems.com/wiki/index.php/Algebra-Complex_Numbers',
		'http://www.exampleproblems.com/wiki/index.php/Algebra-Progressions',
		'http://www.exampleproblems.com/wiki/index.php/Algebra-Functions',
		'http://www.exampleproblems.com/wiki/index.php/Algebra-Binomial_Theorem',
		'http://www.exampleproblems.com/wiki/index.php/Algebra-Rational_Functions',
		
		'http://www.exampleproblems.com/wiki/index.php/Linear_Algebra',
		'http://www.exampleproblems.com/wiki/index.php/Geometry', 
		'http://www.exampleproblems.com/wiki/index.php/Ordinary_Differential_Equations', 
		'http://www.exampleproblems.com/wiki/index.php/Functional_Analysis', 
		'http://www.exampleproblems.com/wiki/index.php/Complex_Variables',
		'http://www.exampleproblems.com/wiki/index.php/Number_Theory',
		
		'http://www.exampleproblems.com/wiki/index.php/PDE:Coordinate_Transformations',
		'http://www.exampleproblems.com/wiki/index.php/PDE:Method_of_characteristics',
		'http://www.exampleproblems.com/wiki/index.php/PDE:Integration_and_Separation_of_Variables',
		'http://www.exampleproblems.com/wiki/index.php/Fourier_Series', 
		'http://www.exampleproblems.com/wiki/index.php/PDE:Mathematical_Modeling', 
		
		'http://www.exampleproblems.com/wiki/index.php/Special_Functions',
		'http://www.exampleproblems.com/wiki/index.php/Fraction_(mathematics)'
	]
	find_number = 1
	for p_p_url in practice_problem_urls:
		response = urllib2.urlopen(p_p_url)
		html = response.read()
		
		primary_tag = p_p_url[find_closest_tag_before_point(p_p_url, "/", len(p_p_url)) + 1:].replace("_"," ").replace("#"," ").replace(":"," ")
		tags = primary_tag
		for s in primary_tag.split(' '):
			tags = tags + ',' + s
		
		first_find = html.find("solution</a>")
		while first_find > 0:
			
			solution_link = find_link(html, first_find)
			solution_url = "http://www.exampleproblems.com" + solution_link
			problem = find_problem_body(html, first_find + 13)
			cleaned = clean_to_latex(problem)
			
			full_heading = find_last_full_h2(html, first_find)
			cleaned = possibly_add_instruction(cleaned, full_heading, html, first_find)
			solution = get_solution_from_url(solution_link)
			problem_tags = construct_tags(tags, html, first_find)
			
			results = {
					'tags': problem_tags,
					'solutionLink':solution_url, 
					'site':"www.exampleproblems.com", 
					'title':primary_tag + " Practice",
					'problem':cleaned,  
					'solution':solution
			}
			
			#print results		
			all_practice_problems.append(results)
			with open(DATA_FILENAME, mode='w') as feedsjson:
				dump(all_practice_problems, feedsjson, indent=4)
				feedsjson.close()
			
			find_number = find_number + 1
			print 'Practice Problem Number ' + str(find_number) + ", currently in " + primary_tag
	
			first_find = html.find("solution</a>", first_find + len(problem))		


def find_last_full_h2(html, original_index):
	index = original_index
	result = -1
	while result == -1 and index > 0:
		result = html.find('<h2', index)
		if result == -1 or result > original_index:
			index = index - 5
			result = -1
	h2_start = result
	h2_end = html.find('</h2>', h2_start)
	return html[h2_start : h2_end + 6]
	
	

def construct_tags(tags, html, first_find):
	problem_tags = tags
	h2_heading = find_last_h2_heading(html, first_find)
	h3_heading = find_last_h3_heading(html, first_find)
	if len(h3_heading) > 0 and html.find(h2_heading) < html.find(h3_heading):
		problem_tags = problem_tags + "," + h3_heading
		h3_heading_modified = h3_heading.replace("-", " ").split(" ")
		if len(h3_heading_modified) > 1:
			for tag in h3_heading_modified:
				if len(tag) > 4:
					problem_tags = problem_tags + "," + tag
	if len(h2_heading) > 0:
		problem_tags = problem_tags + "," + h2_heading		
		h2_heading_modified = h2_heading.replace("-", " ").split(" ")
		if len(h2_heading_modified) > 1:
			for tag in h2_heading_modified:
				if len(tag) > 4:
					problem_tags = problem_tags + "," + tag
	return problem_tags

def possibly_add_instruction(expression, heading, html, index):
	if expression[0:4] == " \( " and expression[-4:] == " \) ":
		heading_index = html.find(heading)
		instruction_index = find_closest_tag_before_point(html, 'For the following problems,', index)
		if heading[-1:] == ".":
			return heading[:-1] + ": "+ expression
		elif heading_index < instruction_index and instruction_index < index:
			next_br = html.find("<br", instruction_index)
			next_p = html.find("</p>", instruction_index)
			if next_br == -1:
				next_br = len(html)
			if next_p == -1:
				next_p = len(html)
			instruction_end_index = min(next_br, next_p)
			if instruction_end_index > index:
				return "Evaluate/Solve " + expression
			else:
				return clean_to_latex(html[instruction_index:instruction_end_index]) + " " + expression 
		else:
			return "Evaluate " + expression
	else:
		return expression

def find_last_h2_heading(html, index):
	heading_start = find_closest_tag_before_point(html, "<h2>", index)
	if heading_start < 0:
		return ""
	span_start = html.find("<span", heading_start)
	heading_end = html.find("</h2>", heading_start)
	if span_start > heading_end or span_start == -1:
		heading_contents = html[heading_start+4:heading_end-4]
		return heading_contents
	else:
		span_open_end = html.find(">", span_start)
		span_real_end = html.find("</span>", span_open_end)
		heading_contents = html[span_open_end+1:span_real_end]
		return heading_contents

def find_last_h3_heading(html, index):
	heading_start = find_closest_tag_before_point(html, "<h3>", index)
	if heading_start < 0:
		return ""
	span_start = html.find("<span", heading_start)
	heading_end = html.find("</h3>", heading_start)
	if span_start > heading_end or span_start == -1:
		heading_contents = html[heading_start+4:heading_end-4]
		return heading_contents
	else:
		span_open_end = html.find(">", span_start)
		span_real_end = html.find("</span>", span_open_end)
		heading_contents = html[span_open_end+1:span_real_end]
		return heading_contents

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
			last_index_to_include = find_closest_tag_before_point(content, "<p>", to_not_include)
			parsed_content = content[content.find('>')+1:last_index_to_include]			
		
		return clean_to_latex(parsed_content)
		
	except urllib2.HTTPError:
		print "Solution not found: " + url
		return None
	except urllib2.URLError:
		print "Solution not found (II): " + url	
		return None


def find_closest_tag_before_point(text, tag, original_index):
	index = original_index
	while index > 0:
		result = text.find(tag, index)
		if result < original_index and result > 0:
			return result
		else:
			index = index - 5
	return -1

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